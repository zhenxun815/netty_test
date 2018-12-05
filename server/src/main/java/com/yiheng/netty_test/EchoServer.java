package com.yiheng.netty_test;

import com.yiheng.netty_test.handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * 〈程序入口〉
 *
 * @author Yiheng
 * @create 2018/12/5
 * @since 1.0.0
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {

        if (args.length != 1) {
            System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
            return;
        }
        int port = Integer.parseInt(args[0]);
        System.out.println("Usage: " + EchoServer.class.getSimpleName() + " " + port);
        new EchoServer(port).start();
    }

    private void start() throws InterruptedException {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                     .channel(NioServerSocketChannel.class)
                     .localAddress(new InetSocketAddress(port))
                     .childHandler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel ch) throws Exception {
                             ch.pipeline()
                               .addLast(serverHandler);
                         }
                     });
            ChannelFuture future = bootstrap.bind()
                                            .sync();

            future.channel()
                  .closeFuture()
                  .sync();
        } finally {
            group.shutdownGracefully()
                 .sync();
        }
    }
}
