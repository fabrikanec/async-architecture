package com.api.gateway.util

import com.github.tomakehurst.wiremock.client.WireMock
import org.junit.jupiter.api.Assertions
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

fun WebTestClient.doGet(url: String) =
    get()
        .uri(url)
        .exchange()
        .expectStatus()
        .isOk
        .expectBody()
        .consumeWith { result ->
            Assertions.assertEquals(url, result.url.path)
        }

fun WebTestClient.doGetNotFound(url: String) =
    get()
        .uri(url)
        .exchange()
        .expectStatus()
        .isNotFound
        .expectBody()
        .consumeWith { result ->
            Assertions.assertEquals(url, result.url.path)
        }

fun WebTestClient.doPost(url: String) =
    post()
        .uri(url)
        .exchange()
        .expectStatus()
        .isOk
        .expectBody()
        .consumeWith { result ->
            Assertions.assertEquals(url, result.url.path)
        }

fun WebTestClient.doPostNotFound(url: String) =
    post()
        .uri(url)
        .exchange()
        .expectStatus()
        .isNotFound
        .expectBody()
        .consumeWith { result ->
            Assertions.assertEquals(url, result.url.path)
        }

fun stubForPostRequest(url: String, data: String) =
    WireMock.stubFor(
        WireMock.post(WireMock.urlEqualTo(url))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(HttpStatus.OK.value())
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withBody(
                        data
                    )
            )
    )

fun stubForGetRequest(url: String, data: String) =
    WireMock.stubFor(
        WireMock.get(WireMock.urlEqualTo(url))
            .willReturn(
                WireMock.aResponse()
                    .withStatus(HttpStatus.OK.value())
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withBody(
                        data
                    )
            )
    )
