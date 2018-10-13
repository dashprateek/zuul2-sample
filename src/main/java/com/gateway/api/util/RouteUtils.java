package com.gateway.api.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;

import com.netflix.config.ConfigurationManager;

public class RouteUtils {

	private static final Map<String, String> routes = RoutesHolder.ROUTES;

	private static final PatternMatcher matcher = new AntPathMatcher();

	public static Map<String, String> getRoutes() {
		return StreamSupport
				.stream(Spliterators.spliteratorUnknownSize(ConfigurationManager.getConfigInstance().getKeys("routes"),
						Spliterator.DISTINCT), false)
				.collect(
						Collectors.toMap(
								key -> StringUtils.removeEndIgnoreCase(
										StringUtils.removeStartIgnoreCase(key, "routes."), ".path"),
								key -> ConfigurationManager.getConfigInstance().getString(key)));
	}

	public static String getRouteVip(String path) {
		return routes.entrySet().stream().filter(entry -> matcher.matches(entry.getValue(), path)).findFirst()
				.map(Entry::getKey).orElse("");
	}

	static class RoutesHolder {
		private static final Map<String, String> ROUTES = getRoutes();
	}

}
