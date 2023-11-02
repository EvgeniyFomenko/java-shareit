package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.BadArgumentsPaginationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> postRequests(long userId, ItemRequestDto requestDto) {
        requestDto.setRequester(userId);
        return post("/", userId, requestDto);
    }

    public ResponseEntity<Object> getRequests(long userId) {
        return get("/", userId);

    }

    public ResponseEntity<Object> getRequestById(long userId, int requestId) {
        return get("/" + requestId, userId);
    }

    public ResponseEntity<Object> getAllRequests(long userId, Integer from, Integer size) {
        if (Objects.isNull(from) || Objects.isNull(size)) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }

        if (from < 0 || size <= 0) {
            throw new BadArgumentsPaginationException("такой страницы не существует");
        }

        Map<String, Object> params = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", userId, params);
    }

}
