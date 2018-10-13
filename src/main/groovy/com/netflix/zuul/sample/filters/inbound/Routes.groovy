/*
 * Copyright 2018 Netflix, Inc.
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */

package com.netflix.zuul.sample.filters.inbound;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;

import com.gateway.api.util.RouteUtils;
import com.netflix.config.ConfigurationManager;
import com.netflix.zuul.context.SessionContext;
import com.netflix.zuul.filters.http.HttpInboundSyncFilter;
import com.netflix.zuul.message.http.HttpRequestMessage;
import com.netflix.zuul.netty.filter.ZuulEndPointRunner;

/**
 * Routes configuration
 *
 * Author: Arthur Gonigberg Date: November 21, 2017
 */
public class Routes extends HttpInboundSyncFilter {


	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter(HttpRequestMessage httpRequestMessage) {
		return true;
	}

	@Override
	public HttpRequestMessage apply(HttpRequestMessage request) {
		SessionContext context = request.getContext();
		context.setEndpoint(ZuulEndPointRunner.PROXY_ENDPOINT_FILTER_NAME);
		context.setRouteVIP(RouteUtils.getRouteVip(request.getPath()));
		return request;
	}
}
