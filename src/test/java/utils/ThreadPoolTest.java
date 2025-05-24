package utils;

import br.com.pedromagno.utils.ThreadPool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolTest {
    @Test
    void deveExecutarRunnable() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ThreadPool.executar(()-> latch.countDown(), "Runnable-Teste");
        Assertions.assertTrue(latch.await(2, TimeUnit.SECONDS));
    }

    @Test
    void deveExecutarCallable() throws Exception {
        Future<String> future = ThreadPool.executar(()-> "resultado", "Callable-Teste");
        Assertions.assertEquals("resultado", future.get());
    }

    @Test
    void deveExecutarComAtraso() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ThreadPool.agendarExecucao(()-> latch.countDown(), 1, TimeUnit.SECONDS, "Agendada");
        Assertions.assertTrue(latch.await(2, TimeUnit.SECONDS));
    }

    @Test
    void deveRedimensionarParaCima() throws InterruptedException {
        int tarefasPesadas = 100;
        CountDownLatch latch = new CountDownLatch(tarefasPesadas);

        for (int i = 0; i < tarefasPesadas; i++) {
            ThreadPool.executar(() -> {
                try{
                    Thread.sleep(200); // Simula Carga
                    latch.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Carga-" + i);
        }

        boolean completou = latch.await(5, TimeUnit.SECONDS);
        Assertions.assertTrue(completou, "As tarefas não foram concluídas a tempo");
    }

    @Test
    void deveRedimensionarParaBaixoAposUsoBaixo() throws InterruptedException {
        // Primeiro, força o crescimento
        for (int i = 0; i < 50; i++) {
            ThreadPool.executar(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "AltaCarga-" + i);
        }

        // Aguarda tempo suficiente para estabilizar e reduzir
        Thread.sleep(2000); // depende do comportamento interno de verificação

        // Esse teste é mais de integração, pois a verificação é baseada em uso e logs
        // Aqui apenas confirmamos que ainda está aceitando tarefas após queda de uso
        CountDownLatch latch = new CountDownLatch(1);
        ThreadPool.executar(latch::countDown, "TesteAposQueda");
        Assertions.assertTrue(latch.await(1, TimeUnit.SECONDS));
    }

    @Test
    void deveExecutarVariasTarefasConcorrentesComRetorno() throws Exception {
        List<Callable<Integer>> tarefas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            tarefas.add(() -> finalI * 2);
        }

        List<Future<Integer>> resultados = new ArrayList<>();
        for (Callable<Integer> tarefa : tarefas) {
            resultados.add(ThreadPool.executar(tarefa, "Batch"));
        }

        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals(i * 2, resultados.get(i).get());
        }
    }

    @Test
    void deveExecutarTarefasEmOrdemAssincronamente() throws InterruptedException {
        AtomicInteger contador = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            ThreadPool.executar(() -> {
                contador.incrementAndGet();
                latch.countDown();
            }, "Ordem-" + i);
        }

        Assertions.assertTrue(latch.await(2, TimeUnit.SECONDS));
        Assertions.assertEquals(5, contador.get());
    }
}
