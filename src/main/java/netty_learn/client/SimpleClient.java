package netty_learn.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @filename SimpleClient
 * @description
 * @author朱愈曌
 * @date 2019/5/16 15:56
 */
public class SimpleClient {
	public void connect(String host,int port) throws InterruptedException {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel socketChannel) throws Exception {
					socketChannel.pipeline().addLast(new SimpleClientHandler());
				}
			});
			//start the client
			ChannelFuture f = b.connect(host, port).sync();
			//Wait until the connection is closed
			f.channel().closeFuture().sync();

		}finally {
			workerGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		SimpleClient client = new SimpleClient();
		client.connect("127.0.0.1",9999);
	}
}
