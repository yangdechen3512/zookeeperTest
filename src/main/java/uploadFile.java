import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.*;

/**
 * Created by telin on 2017/9/6.
 */
public class uploadFile
{
	public static void main(String[] args)
	{
		uploadFile uploadFile = new uploadFile();
		try{
			uploadFile.uploadFileToZK();
			uploadFile.get();
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public void uploadFileToZK() throws KeeperException, InterruptedException
	{
		String propFilePath = "src\\main\\file\\macAdress.txt";

		ZooKeeper zk = null;
		try
		{
			zk = new ZooKeeper(
					"192.168.8.199:2181",
					300000, new Watcher()
			{
				// 监控所有被触发的事件
				public void process(WatchedEvent event)
				{
					System.out.println("已经触发了" + event.getType() + "事件！");
				}
			});
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (zk.exists("/flume", true) == null)
		{
			zk.create("/flume", null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
		}
		InputStream is = null;
		ByteArrayOutputStream bytestream = null;
		byte[] data = null;
		try
		{
			is = new FileInputStream(propFilePath);
			bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = is.read()) != -1)
			{
				bytestream.write(ch);
			}
			data = bytestream.toByteArray();
			System.out.println(new String(data));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				bytestream.close();
				is.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		// 创建一个目录节点
		Stat stat = zk.exists("/flume/macAdress", true);
		if (stat == null)
		{
			zk.create("/flume/macAdress", data, ZooDefs.Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
		}
		else
		{
			zk.delete("/flume/macAdress", stat.getVersion());
			zk.create("/flume/macAdress", data, ZooDefs.Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
		}
	}

	public void get() throws KeeperException, InterruptedException
	{
		ZooKeeper zk = null;
		try
		{
			if(zk != null){
				zk.close();
		}

			zk = new ZooKeeper(
					"192.168.88.199:2181",
					300000, new Watcher()
			{
				// 监控所有被触发的事件
				public void process(WatchedEvent event)
				{
					System.out.println("已经触发了" + event.getType() + "事件！");
				}
			});
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println(new String(zk.getData("/flume/macAdress", true, null)));
	}
}
