package com.universe.touchpoint.config.transport.rpc;

import com.universe.touchpoint.config.transport.RPCConfig;

import java.lang.reflect.Method;

public class DubboConfig extends RPCConfig {

    /**
     * Interface class, default value is void.class
     */
    public Class<?> interfaceClass;

    /**
     * Interface class name, default value is empty public String
     */
    public String interfaceName;

    /**
     * Service version, default value is empty public String
     */
    public String version;

    /**
     * Service group, default value is empty public String
     */
    public String group;

    /**
     * Service path, default value is empty public String
     */
    public String path;

    /**
     * Whether to export service, default value is true
     */
    boolean export;

    /**
     * Service token, default value is empty public String
     */
    public String token;

    /**
     * Whether the service is deprecated, default value is false
     */
    public boolean deprecated;

    /**
     * Whether the service is dynamic, default value is true
     */
    public boolean dynamic;

    /**
     * Access log for the service, default value is empty public String
     */
    public String accesslog;

    /**
     * Maximum concurrent executes for the service, default value is -1 - no limits
     */
    public int executes;

    /**
     * Whether to register the service to register center, default value is true
     */
    public boolean register;

    /**
     * Service weight value, default value is -1
     */
    public int weight;

    /**
     * Service doc, default value is empty public String
     */
    public String document;

    /**
     * Delay time for service registration, default value is -1
     */
    public int delay;

    public String local;

    /**
     * Service stub name, use interface name + Local if not set
     */
    public String stub;

    /**
     * Cluster strategy, legal values include: failover, failfast, failsafe, failback, forking
     * you can use {@link org.apache.dubbo.common.constants.ClusterRules#FAIL_FAST} ……
     */
    public String cluster;

    /**
     * How the proxy is generated, legal values include: jdk, javassist
     */
    public String proxy;

    /**
     * Maximum connections service provider can accept, default value is -1 - connection is shared
     */
    public int connections;

    /**
     * The callback instance limit peer connection
     * <p>
     * see org.apache.dubbo.common.constants.CommonConstants.DEFAULT_CALLBACK_INSTANCES
     */
    public int callbacks;

    /**
     * Callback method name when connected, default value is empty public String
     */
    public String onconnect;

    /**
     * Callback method name when disconnected, default value is empty public String
     */
    public String ondisconnect;

    /**
     * Service owner, default value is empty public String
     */
    public String owner;

    /**
     * Service layer, default value is empty public String
     */
    public String layer;

    /**
     * Service invocation retry times
     *
     * @see org.apache.dubbo.common.constants.CommonConstants#DEFAULT_RETRIES
     */
    public int retries;

    /**
     * Load balance strategy, legal values include: random, roundrobin, leastactive
     *
     * you can use {@link org.apache.dubbo.common.constants.LoadbalanceRules#RANDOM} ……
     */
    public String loadbalance;

    /**
     * Whether to enable async invocation, default value is false
     */
    public boolean async;

    /**
     * Maximum active requests allowed, default value is -1
     */
    public int actives;

    /**
     * Whether the async request has already been sent, the default value is false
     */
    public boolean sent;

    /**
     * Service mock name, use interface name + Mock if not set
     */
    public String mock;

    /**
     * Whether to use JSR303 validation, legal values are: true, false
     */
    public String validation;

    /**
     * Timeout value for service invocation, default value is -1
     */
    public int timeout;

    /**
     * Specify cache implementation for service invocation, legal values include: lru, threadlocal, jcache
     */
    public String cache;

    public String[] filter;

    public String[] listener;

    public String[] parameters;

    /**
     * Application spring bean name
     * @deprecated This attribute was deprecated, use bind application/module of spring ApplicationContext
     */
    @Deprecated
    public String application;

    /**
     * Module spring bean name
     */
    public String module;

    /**
     * Provider spring bean name
     */
    public String provider;

    /**
     * Protocol spring bean names
     */
    public String[] protocol;

    /**
     * Monitor spring bean name
     */
    public String monitor;

    /**
     * Registry spring bean name
     */
    public String[] registry;

    /**
     * Service tag name
     */
    public String tag;

    /**
     * methods support
     *
     * @return
     */
    public Method[] methods;

    /**
     * the scope for referring/exporting a service, if it's local, it means searching in current JVM only.
     * @see org.apache.dubbo.rpc.Constants#SCOPE_LOCAL
     * @see org.apache.dubbo.rpc.Constants#SCOPE_REMOTE
     */
    public String scope;

    /**
     * Weather the service is export asynchronously
     */
    public boolean exportAsync;

    /**
     * bean name of service executor(thread pool), used for thread pool isolation between services
     * @return
     */
    public String executor;

    /**
     * Payload max length.
     */
    public String payload;

    /**
     * The serialization type
     */
    public String serialization;

    /**
     * If the parameter has a value, the consumer will read the parameter first.
     * If the Dubbo Sdk you are using contains the serialization type, the serialization method specified by the argument is used.
     * <p>
     * When this parameter is null or the serialization type specified by this parameter does not exist in the Dubbo SDK, the serialization type specified by serialization is used.
     * If the Dubbo SDK if still does not exist, the default type of the Dubbo SDK is used.
     * For Dubbo SDK >= 3.2, <code>preferSerialization</code> takes precedence over <code>serialization</code>
     * <p>
     * The configuration supports multiple, which are separated by commas.Such as:<code>fastjson2,fastjson,hessian2</code>
     */
    public String preferSerialization;

    public DubboConfig(String applicationName, String registryAddress) {
        super(applicationName, registryAddress);
    }

}
