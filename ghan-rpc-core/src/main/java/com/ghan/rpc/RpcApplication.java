package com.ghan.rpc;

import com.ghan.rpc.config.RegistryConfig;
import com.ghan.rpc.config.RpcConfig;
import com.ghan.rpc.constant.RpcConstant;
import com.ghan.rpc.registry.Registry;
import com.ghan.rpc.registry.RegistryFactory;
import com.ghan.rpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * RPC 框架应用
 * 相当于 holder，存放了项目全局用到的变量。双检锁单例模式实现
 */
@Slf4j
public class RpcApplication {

    /**
     * 可见性：
     *
     * 当一个变量被声明为 volatile，它的修改对于所有线程都是立即可见的。这意味着如果一个线程更新了 volatile 变量的值，其他线程能够立刻看到这个更新，而不会出现缓存延迟的问题。
     * 在没有 volatile 修饰的情况下，线程可能会读取到旧的缓存值，因为 Java 的内存模型允许线程将变量存储在各自的工作内存中，而不是直接访问主内存中的变量。
     *
     * 禁止指令重排：
     *
     * volatile 变量的写操作会被禁止重排到该变量的读操作之前。
     * 对于 volatile 变量，Java 内存模型会保证写操作在内存中可见，并且其他线程读取到的值总是最新的。
     */
    private static volatile RpcConfig rpcConfig;

    /**
     * 框架初始化，支持传入自定义配置
     *
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("rpc init, config = {}", newRpcConfig.toString());
        // 注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init, config = {}", registryConfig);

        // 创建并注册shutdown hook,JVM退出时候执行操作
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("rpc shutdown");
            registry.destroy();
        }));
    }


    /**
     * 初始化-加载默认配置
     */
    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            // 配置加载失败，使用默认值
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }


    /**
     * 获取配置
     *
     * @return
     */
    public static RpcConfig getRpcConfig() {
        //if (rpcConfig == null)：首先检查 rpcConfig 是否为 null。
        // 如果不为 null，说明 RpcConfig 已经被初始化过，直接返回它即可。这是为了提高效率，避免不必要的同步。
        if (rpcConfig == null) {
            //synchronized (RpcApplication.class)：如果 rpcConfig 为 null，则进入同步块，
            // 确保只有一个线程能够进入这个代码块。
            synchronized (RpcApplication.class) {
                // 在同步块内部，再次检查 rpcConfig 是否为 null。
                // 这是因为在进入同步块的过程中，可能其他线程已经初始化了 rpcConfig。
                // 这种双重检查确保只有第一个到达这个点的线程会初始化 rpcConfig，后续线程会跳过初始化。
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
