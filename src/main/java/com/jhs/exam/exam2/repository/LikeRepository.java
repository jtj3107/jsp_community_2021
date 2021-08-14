package com.jhs.exam.exam2.repository;

import com.jhs.exam.exam2.container.ContainerComponent;
import com.jhs.exam.exam2.dto.IsLike;
import com.jhs.mysqliutil.MysqlUtil;
import com.jhs.mysqliutil.SecSql;

public class LikeRepository implements ContainerComponent {

	@Override
	public void init() {

	}

	public IsLike getLikeByArticleIdAndMemberId(int articleId, int memberId) {
		SecSql sql = new SecSql();
		sql.append("SELECT L.*");
		sql.append("FROM islike AS L");
		sql.append("WHERE L.articleId = ?", articleId);
		sql.append("AND L.memberId = ?", memberId);
		sql.append("AND L.islike = 1");
		
		return MysqlUtil.selectRow(sql, IsLike.class);
	}

	public void likeInsert(int articleId, int memberId) {
		SecSql sql = new SecSql();
		sql.append("INSERT INTO isLike");
		sql.append("SET regDate = NOW()");
		sql.append(", articleId = ?", articleId);
		sql.append(", memberId = ?", memberId);

		MysqlUtil.insert(sql);
	}

	public void likeDelete(int articleId, int memberId) {
		SecSql sql = new SecSql();
		sql.append("DELETE FROM islike");
		sql.append("WHERE articleId = ?", articleId);
		sql.append("AND memberId = ?", memberId);
		sql.append("AND islike = 1");

		MysqlUtil.delete(sql);
	}

	public IsLike getDisLikeByArticleIdAndMemberId(int articleId, int memberId) {
		SecSql sql = new SecSql();
		sql.append("SELECT L.*");
		sql.append("FROM islike AS L");
		sql.append("WHERE L.articleId = ?", articleId);
		sql.append("AND L.memberId = ?", memberId);
		sql.append("AND L.islike = 0");

		return MysqlUtil.selectRow(sql, IsLike.class);
	}

	public void disLikeInsert(int articleId, int memberId) {
		SecSql sql = new SecSql();
		sql.append("INSERT INTO isLike");
		sql.append("SET regDate = NOW()");
		sql.append(", articleId = ?", articleId);
		sql.append(", memberId = ?", memberId);
		sql.append(", islike = 0");

		MysqlUtil.insert(sql);
		
	}

	public void disLikeDelete(int articleId, int memberId) {
		SecSql sql = new SecSql();
		sql.append("DELETE FROM isLike");
		sql.append("WHERE articleId = ?", articleId);
		sql.append("AND memberId = ?", memberId);
		sql.append("AND islike = 0");

		MysqlUtil.delete(sql);
		
	}

}
