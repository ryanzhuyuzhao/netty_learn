package netty_learn.server;



import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * @filename HelloServerHandler
 * @description 输出接收到的消息
 * @author朱愈曌
 * @date 2019/5/16 14:35
 */
public class HelloServerHandler extends ChannelInboundHandlerAdapter {
	/**
	 * 功能描述	收到数据时调用
	 * @param ctx
	 * @param msg
	 * @return void
	 * @author朱愈曌
	 * @date 2019/5/16 14:41
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try{
			ByteBuf in = (ByteBuf)msg;
			System.out.println(in.toString(CharsetUtil.UTF_8));
		}finally {
			//抛弃接收到的数据
			ReferenceCountUtil.release(msg);
		}
	}

	/**
	 * 功能描述 当Netty由于IO错误或者处理器在处理事件时抛出异常时调用
	 * @param ctx
	 * @param cause
	 * @return void
	 * @author朱愈曌
	 * @date 2019/5/16 14:43
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		//当出现异常就关闭连接
		cause.printStackTrace();
		ctx.close();
	}
}
