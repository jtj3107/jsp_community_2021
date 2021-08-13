package com.jhs.exam.exam2.repository;

import java.util.List;

import com.jhs.exam.exam2.container.ContainerComponent;
import com.jhs.exam.exam2.dto.Reply;
import com.jhs.mysqliutil.MysqlUtil;
import com.jhs.mysqliutil.SecSql;

public class ReplyRepository implements ContainerComponent{

	@Override
	public void init() {
		
	}

	// 댓글을 DB에 저장하는 메서드
	public void write(int memberId, int articleId, String body) {
		SecSql sql = new SecSql();
		sql.append("INSERT INTO reply");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", memberId = ?", memberId);
		sql.append(", articleId = ?", articleId);
		sql.append(", body = ?", body);

		MysqlUtil.insert(sql);
	}

	// DB에서 댓글 rows값을 리턴하는 메서드
	public List<Reply> getForPrintReplies(int articleId) {
		SecSql sql = new SecSql();
		sql.append("SELECT R.*");
		sql.append(", IFNULL(M.nickname, '삭제된회원') AS extra__writerName");
		sql.append("FROM reply AS R");
		sql.append("LEFT JOIN `member` AS M");
		sql.append("ON R.memberId = M.id");
		sql.append("WHERE articleId = ?", articleId);
		sql.append("ORDER BY R.id DESC");
		
		return MysqlUtil.selectRows(sql, Reply.class);
	}

}
