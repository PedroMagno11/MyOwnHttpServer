package br.com.pedromagno.utils;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AccessTracker {
    private static AccessTracker instance;

    private final AtomicInteger totalAcessos;
    private final AtomicInteger conexoesAtivas;
    private final Set<String> visitantesUnicos;

    private AccessTracker() {
        totalAcessos = new AtomicInteger(0);
        conexoesAtivas = new AtomicInteger(0);
        visitantesUnicos = ConcurrentHashMap.newKeySet();
    }

    public static AccessTracker getInstance() {
        if (instance == null) {
            instance = new AccessTracker();
        }
        return instance;
    }

    public void incrementarAcessos() {
        totalAcessos.incrementAndGet();
    }

    public void novaConexao() {
        conexoesAtivas.incrementAndGet();
    }

    public void fecharConexao(){
        conexoesAtivas.decrementAndGet();
    }

    public void registarVisitante(String ipVisitante) {
        visitantesUnicos.add(ipVisitante);
    }

    public int getTotalAcessos() {
        return totalAcessos.get();
    }

    public int getConexoesAtivas() {
        return conexoesAtivas.get();
    }

    public int getVisitantesUnicos(){
        return visitantesUnicos.size();
    }
}
