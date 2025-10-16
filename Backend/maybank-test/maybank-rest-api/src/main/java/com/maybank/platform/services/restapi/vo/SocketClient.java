package com.maybank.platform.services.restapi.vo;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class SocketClient {
    public static final ConcurrentHashMap<String, UUID> concurrentHashMap = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<UUID, ChartClientData> clientModes = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<UUID, Integer> clientBrands = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<UUID, Integer> clientLocations = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<UUID, Integer> clientCountry = new ConcurrentHashMap<>();

    public List<UUID> findAll() {
        return CollUtil.newArrayList(concurrentHashMap.values());
    }

    public Optional<UUID> findByUserToken(String token) {
        return Optional.ofNullable(concurrentHashMap.get(token));
    }

    public void save(String userId, UUID sessionId) {
        concurrentHashMap.put(userId, sessionId);
    }

    public void deleteByToken(String token) {
        concurrentHashMap.remove(token);
    }

    public Optional<ChartClientData> findClientModeByUUID(UUID sessionId)  {
        return Optional.ofNullable(clientModes.get(sessionId));
    }

    public Optional<Integer> findClientBrandByUUID(UUID sessionId)  {
        return Optional.ofNullable(clientBrands.get(sessionId));
    }

    public Optional<Integer> findClientLocationByUUID(UUID sessionId)  {
        return Optional.ofNullable(clientLocations.get(sessionId));
    }

    public Optional<Integer> findClientCountryByUUID(UUID sessionId)  {
        return Optional.ofNullable(clientCountry.get(sessionId));
    }

    public void saveClientMode(UUID sessionId, ChartClientData data) {
        clientModes.put(sessionId, data);
    }

    public void saveClientBrand(UUID sessionId, Integer data) {
        clientBrands.put(sessionId, data);
    }

    public void saveClientLocation(UUID sessionId, Integer data) {
        clientLocations.put(sessionId, data);
    }

    public void saveClientCountry(UUID sessionId, Integer data) {
        clientCountry.put(sessionId, data);
    }

    public void deleteClientModeByUUID(UUID sessionId) {
        clientModes.remove(sessionId);
    }

    public void deleteClientBrandByUUID(UUID sessionId) {
        clientBrands.remove(sessionId);
    }

    public void deleteClientLocationByUUID(UUID sessionId) {
        clientLocations.remove(sessionId);
    }
    public void deleteClientCountryByUUID(UUID sessionId) {
        clientCountry.remove(sessionId);
    }

    public ConcurrentHashMap<UUID, ChartClientData> getClientModes() {
        return clientModes;
    }

    public ConcurrentHashMap<UUID, Integer> getClientBrands() {
        return clientBrands;
    }

    public ConcurrentHashMap<UUID, Integer> getClientLocations() {
        return clientLocations;
    }

    public ConcurrentHashMap<UUID, Integer> getClientCountry() {
        return clientCountry;
    }

    public Map<String, List<UUID>> groupUUIDsByMode() {
        return clientModes.entrySet().stream()
                .collect(Collectors.groupingBy(
                        entry -> entry.getValue().getMode()+"|"+entry.getValue().getInput(),
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())
                ));
    }
}
