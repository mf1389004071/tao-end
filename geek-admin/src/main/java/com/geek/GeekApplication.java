package com.geek;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动程序
 * 
 * @author geek
 */
@EnableAsync
@EnableCaching
@MapperScan(basePackages = { "com.geek.**.mapper" })
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class }, scanBasePackages = { "com.geek", "com.anji.captcha" })
public class GeekApplication {
    public static void main(String[] args) throws UnknownHostException {
        ensureLogDir();
        // System.setProperty("spring.devtools.restart.enabled", "false");
        ConfigurableApplicationContext application = SpringApplication.run(GeekApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  极客启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'     " + " ____           _         " + "\n" +
                " |  |\\ \\  |  ||   |(_,_)'    " + "  / ___| ___  ___| | __   " + "\n" +
                " |  | \\ `'   /|   `-'  /      " + "| |  _ / _ \\/ _ \\ |/ /  " + "\n" +
                " |  |  \\    /  \\      /      " + " | |_| |  __/  __/   <    " + "\n" +
                " ''-'   `'-'    `-..-'         " + "\\____|\\___|\\___|_|\\_\\");

        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        System.out.println("\n----------------------------------------------------------\n" +
                " Application geek-Geek is running! Access URLs:\n" +
                " Local:        http://localhost:" + port + "/\n" +
                " External:     http://" + ip + ":" + port + "/\n" +
                " Swagger文档:  http://" + ip + ":" + port + "/swagger-ui/index.html\n" +
                " Knife4j文档:  http://" + ip + ":" + port + "/doc.html" + "" + "\n" +
                "----------------------------------------------------------");
    }

    /** 在加载任何日志组件前创建日志目录，避免 logback 写文件时报 No such file or directory */
    private static void ensureLogDir() {
        String path = System.getProperty("log.path");
        if (path == null || path.isEmpty()) {
            path = Paths.get(System.getProperty("user.dir"), "logs").toAbsolutePath().toString();
            System.setProperty("log.path", path);
        }
        try {
            Path dir = Paths.get(path);
            if (!Files.isDirectory(dir)) {
                Files.createDirectories(dir);
            }
        } catch (Exception e) {
            System.err.println("WARN: 无法创建日志目录 " + path + "，将可能影响文件日志输出: " + e.getMessage());
        }
    }
}
