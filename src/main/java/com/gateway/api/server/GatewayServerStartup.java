package com.gateway.api.server;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.config.DynamicIntProperty;
import com.netflix.discovery.EurekaClient;
import com.netflix.netty.common.accesslog.AccessLogPublisher;
import com.netflix.netty.common.channel.config.ChannelConfig;
import com.netflix.netty.common.channel.config.CommonChannelConfigKeys;
import com.netflix.netty.common.metrics.EventLoopGroupMetrics;
import com.netflix.netty.common.proxyprotocol.StripUntrustedProxyHeadersHandler;
import com.netflix.netty.common.status.ServerStatusManager;
import com.netflix.spectator.api.Registry;
import com.netflix.zuul.FilterLoader;
import com.netflix.zuul.FilterUsageNotifier;
import com.netflix.zuul.RequestCompleteHandler;
import com.netflix.zuul.context.SessionContextDecorator;
import com.netflix.zuul.netty.server.BaseServerStartup;
import com.netflix.zuul.netty.server.DirectMemoryMonitor;
import com.netflix.zuul.netty.server.ZuulServerChannelInitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;

public class GatewayServerStartup extends BaseServerStartup {

	@Inject
	public GatewayServerStartup(ServerStatusManager serverStatusManager, FilterLoader filterLoader,
			SessionContextDecorator sessionCtxDecorator, FilterUsageNotifier usageNotifier,
			RequestCompleteHandler reqCompleteHandler, Registry registry, DirectMemoryMonitor directMemoryMonitor,
			EventLoopGroupMetrics eventLoopGroupMetrics, EurekaClient discoveryClient,
			ApplicationInfoManager applicationInfoManager, AccessLogPublisher accessLogPublisher) {
		super(serverStatusManager, filterLoader, sessionCtxDecorator, usageNotifier, reqCompleteHandler, registry,
				directMemoryMonitor, eventLoopGroupMetrics, discoveryClient, applicationInfoManager,
				accessLogPublisher);
	}

	@Override
	protected Map<Integer, ChannelInitializer> choosePortsAndChannels(ChannelGroup clientChannels,
			ChannelConfig channelDependencies) {

		Map<Integer, ChannelInitializer> portsToChannels = new HashMap<>();

		int port = new DynamicIntProperty("zuul.server.port.main", 7001).get();

		ChannelConfig channelConfig = BaseServerStartup.defaultChannelConfig();
		/*
		 * These settings may need to be tweaked depending if you're running
		 * behind an ELB HTTP listener, TCP listener, or directly on the
		 * internet.
		 *
		 * The below settings can be used when running behind an ELB HTTP
		 * listener that terminates SSL for you and passes XFF headers.
		 */
		channelConfig.set(CommonChannelConfigKeys.allowProxyHeadersWhen,
				StripUntrustedProxyHeadersHandler.AllowWhen.ALWAYS);
		channelConfig.set(CommonChannelConfigKeys.preferProxyProtocolForClientIp, false);
		channelConfig.set(CommonChannelConfigKeys.isSSlFromIntermediary, false);
		channelConfig.set(CommonChannelConfigKeys.withProxyProtocol, false);

		portsToChannels.put(port,
				new ZuulServerChannelInitializer(port, channelConfig, channelDependencies, clientChannels));
		logPortConfigured(port, null);

		return portsToChannels;
	}

}
