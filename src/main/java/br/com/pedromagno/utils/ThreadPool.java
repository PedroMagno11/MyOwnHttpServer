package br.com.pedromagno.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Classe utilitária responsável pela gestão centralizada do pool de threads.
 * Garante uso eficiente dos núcleos do processador e evita criação de threads soltas.
 */
public class ThreadPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPool.class);
    private static ThreadPool instance;
    private final ScheduledThreadPoolExecutor executor;
    private final int tamanhoMinimo;

    private ThreadPool() {
        tamanhoMinimo = Runtime.getRuntime().availableProcessors();
        executor = new ScheduledThreadPoolExecutor(tamanhoMinimo);
        executor.setMaximumPoolSize(20000); // Limite alto para cenários com alta concorrência
    }

    public static synchronized ThreadPool getInstance() {
        if (instance == null) {
            instance = new ThreadPool();
        }
        return instance;
    }

    public static Future<?> executar(Runnable tarefa) {
        return executar(tarefa, null);
    }

    public static Future<?> executar(Runnable tarefa, String nome) {
        ThreadPool pool = getInstance();
        pool.verificarEstadoPool();
        if (nome != null) {
            LOGGER.debug("Thread '{}' enviada para o pool.", nome);
        }
        return pool.executor.submit(tarefa);
    }

    public static <T> Future<T> executar(Callable<T> tarefa, String nome) {
        ThreadPool pool = getInstance();
        pool.verificarEstadoPool();
        if (nome != null) {
            LOGGER.debug("Thread '{}' enviada para o pool.", nome);
        }
        return pool.executor.submit(tarefa);
    }

    public static Future<?> agendarExecucao(Runnable tarefa, long atraso, TimeUnit unidade, String nome) {
        ThreadPool pool = getInstance();
        pool.verificarEstadoPool();
        if (nome != null) {
            LOGGER.debug("Thread '{}' agendada para execução em {} {}.", nome, atraso, unidade);
        }
        return pool.executor.schedule(tarefa, atraso, unidade);
    }

    public static ScheduledFuture<?> agendarExecucaoPeriodica(Runnable tarefa, long frequencia, String nome) {
        return agendarExecucaoPeriodica(tarefa, 0, frequencia, TimeUnit.MILLISECONDS, nome);
    }

    public static ScheduledFuture<?> agendarExecucaoPeriodica(Runnable tarefa, long frequencia, TimeUnit unidade, String nome) {
        return agendarExecucaoPeriodica(tarefa, 0, frequencia, unidade, nome);
    }

    public static ScheduledFuture<?> agendarExecucaoPeriodica(Runnable tarefa, long atrasoInicial, long frequencia, TimeUnit unidade, String nome) {
        ThreadPool pool = getInstance();
        pool.verificarEstadoPool();
        LOGGER.info("Thread '{}' agendada para execução periódica a cada {} {}.", nome, frequencia, unidade);
        return pool.executor.scheduleAtFixedRate(tarefa, atrasoInicial, frequencia, unidade);
    }

    private void verificarEstadoPool() {
        int ativos = executor.getActiveCount();
        int poolAtual = executor.getCorePoolSize();
        double uso = (double) ativos / poolAtual * 100;

        if (uso > 75) {
            int novoTamanho = poolAtual * 2;
            executor.setCorePoolSize(novoTamanho);
            LOGGER.info("Uso alto de threads ({}%). Pool aumentado: {} -> {}.", (int) uso, poolAtual, novoTamanho);
        } else if (uso < 10) {
            int novoTamanho = Math.max(poolAtual / 2, tamanhoMinimo);
            if (novoTamanho != poolAtual) {
                executor.setCorePoolSize(novoTamanho);
                LOGGER.info("Uso baixo de threads ({}%). Pool reduzido: {} -> {}.", (int) uso, poolAtual, novoTamanho);
            }
        }
    }
}
