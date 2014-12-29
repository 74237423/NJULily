package log.ui.logmsg;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import log.LogMessage;
import log.LogMsgController;
import log.config.LogUIConfig;

/**
 * 把日志信息放到TextArea中
 * @author cylong
 * @version 2014年12月26日 下午9:19:12
 */
public class LogTextArea extends JScrollPane {

	/** serialVersionUID */
	private static final long serialVersionUID = 3963201247039258871L;

	/** 真正的TextArea在这 */
	private JTextArea textArea;

	/** 边框标题 */
	private String title;

	public LogTextArea() {
		this.setOpaque(false);	// 将JScrollPane设置为透明
		this.getViewport().setOpaque(false);	// 将中间的viewport设置为透明
		this.addTextArea();	// 添加文本域
		this.setBorder();	// 设置边框
		this.modifyScrollBar();	// 修改滚动条样式
	}

	private void modifyScrollBar() {
		JScrollBar bar = this.getVerticalScrollBar();
		bar.setBackground(Color.LIGHT_GRAY);
		bar.setOpaque(false);
		bar.setBorder(new EmptyBorder(0, 0, 0, 0));
	}

	private void addTextArea() {
		textArea = new JTextArea();
		// 最新的在前面显示
		for(int i = LogMsgController.logs.size() - 1; i >= 0; i--) {
			LogMessage log = LogMsgController.logs.get(i);
			textArea.append(log.toString() + "\r\n");
		}
		textArea.setLineWrap(true);
		textArea.setFont(LogUIConfig.TEXT_FONT);
		textArea.setOpaque(false);
		textArea.setEditable(false);
		this.setViewportView(textArea);
	}

	private void setBorder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		title = sdf.format(new Date()); // 初始化标题为当前日期
		this.setBorder(BorderFactory.createTitledBorder(null, title, TitledBorder.CENTER, TitledBorder.TOP, LogUIConfig.TEXT_FONT));
	}

	public void setTitle(String title) {
		this.title = title;
		this.setBorder(null);
		this.setBorder(BorderFactory.createTitledBorder(getBorder(), title, TitledBorder.CENTER, TitledBorder.TOP, LogUIConfig.TEXT_FONT));
		this.repaint();
	}

	/**
	 * 将存放在ArrayList里的LogMessage显示在TextArea中
	 * @param logs
	 * @author cylong
	 * @version 2014年12月27日 上午4:40:09
	 */
	public void append(ArrayList<LogMessage> logs) {
		if (logs == null) {
			return;
		}
		for(int i = logs.size() - 1; i >= 0; i--) {
			LogMessage log = logs.get(i);
			textArea.append(log.toString() + "\r\n");
		}
	}

	public void append(String text) {
		textArea.append(text);
	}

	public void setText(String text) {
		textArea.setText(text);
	}

}