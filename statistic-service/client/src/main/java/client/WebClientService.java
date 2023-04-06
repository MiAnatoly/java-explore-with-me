package client;

import dto.HitDto;
import dto.ViewStats;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class WebClientService {
    private final WebClient webClient;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final LocalDateTime MIN_DATE = LocalDateTime
            .of(1970, 1,1,0, 0, 0);
    private static final LocalDateTime MAX_DATE = LocalDateTime
            .of(3000, 1,1,0, 0, 0);

    public WebClientService(@Value("${stats-server.url}") String serverUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(serverUrl).build();
    }

    public ClientResponse add(HitDto hitDto) {
        return webClient.post().uri("/hit")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(hitDto), HitDto.class)
                .exchange()
                .block();
    }

    public List<ViewStats> find(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/stats")
                        .queryParam("start", start.format(DATE_TIME_FORMATTER))
                        .queryParam("end", end.format(DATE_TIME_FORMATTER))
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .bodyToFlux(ViewStats.class)
                .collectList()
                .block();
    }

    public List<ViewStats> find(List<String> uris, Boolean unique) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/stats")
                        .queryParam("start", MIN_DATE.format(DATE_TIME_FORMATTER))
                        .queryParam("end", MAX_DATE.format(DATE_TIME_FORMATTER))
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .bodyToFlux(ViewStats.class)
                .collectList()
                .block();
    }

    public List<ViewStats> find(Boolean unique) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/stats")
                        .queryParam("start", MIN_DATE.format(DATE_TIME_FORMATTER))
                        .queryParam("end", MAX_DATE.format(DATE_TIME_FORMATTER))
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .bodyToFlux(ViewStats.class)
                .collectList()
                .block();
    }

    public List<ViewStats> find(LocalDateTime start, LocalDateTime end, Boolean unique) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/stats")
                        .queryParam("start", start.format(DATE_TIME_FORMATTER))
                        .queryParam("end", end.format(DATE_TIME_FORMATTER))
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .bodyToFlux(ViewStats.class)
                .collectList()
                .block();
    }
}
