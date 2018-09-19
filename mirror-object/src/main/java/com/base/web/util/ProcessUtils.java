package com.base.web.util;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 *  Process设置超时时间5秒
 */
public class ProcessUtils {
    public static int executeCommand(final List<String> command) throws InterruptedException, TimeoutException, IOException {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(command);
        builder.redirectErrorStream(true);
        Process process = builder.start();
        Worker worker = new Worker(process);
        worker.start();
        try {
            worker.join(5000);
            if (worker.exit != null){
                return worker.exit;
            } else{
                throw new TimeoutException();
            }
        } catch (InterruptedException ex) {
            worker.interrupt();
            Thread.currentThread().interrupt();
            throw ex;
        } finally {
            process.destroy();
        }
    }


    private static class Worker extends Thread {
        private final Process process;
        private Integer exit;

        private Worker(Process process) {
            this.process = process;
        }

        @Override
        public void run() {
            try {
                exit = process.waitFor();
            } catch (InterruptedException ignore) {
                return;
            }
        }
    }
}
