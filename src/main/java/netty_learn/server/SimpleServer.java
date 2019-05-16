package netty_learn.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @filename SimpleServer
 * @description
 * @author朱愈曌
 * @date 2019/5/16 15:08
 */
public class SimpleServer {
	private int port;

	public SimpleServer(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		//EventLoopGroup时用来处理IO操作的多线程事件循环器
		//bossGroup用来接收进来的连接
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		//workerGroup用来处理已经被接收的连接
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			//启动NIO服务的辅助启动类
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)//配置Channel
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socketChannel) throws Exception {
					//注册handler
					socketChannel.pipeline().addLast(new SimpleServerHandler());
				}
			})
					.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);
			//绑定端口，开始接收进来的连接
			ChannelFuture f = b.bind(port).sync();
			//等待服务器socket关闭
			f.channel().closeFuture().sync();
		}finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		new SimpleServer(9999).run();
	}
}
