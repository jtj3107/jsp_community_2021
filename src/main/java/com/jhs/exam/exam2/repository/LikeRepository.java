package com.jhs.exam.exam2.repository;

import com.jhs.exam.exam2.container.ContainerComponent;
import com.jhs.exam.exam2.dto.IsLike;
import com.jhs.mysqliutil.MysqlUtil;
import com.jhs.mysqliutil.SecSql;

public class LikeRepository implements ContainerComponent {

	@Override
	public void init() {

	}

	public IsLike getLikeByArticleIdAndMemberId(String relTypeCode, int relId, int memberId) {
		SecSql sql = new SecSql();
		sql.append("SELECT L.*");
		sql.append("FROM islike AS L");
		sql.append("WHERE L.relTypeCode = ?", relTypeCode);
		sql.append("AND L.relId = ?", relId);
		sql.append("AND L.memberId = ?", memberId);
		sql.append("AND L.point = 1");
		
		return MysqlUtil.selectRow(sql, IsLike.class);
	}

	public void likeInsert(String relTypeCode, int relId, int memberId) {
		SecSql sql = new SecSql();
		sql.append("INSERT INTO isLike");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", relTypeCode = ?", relTypeCode);
		sql.append(", relId = ?", relId);
		sql.append(", memberId = ?", memberId);
		sql.append(", `point` = 1");

		MysqlUtil.insert(sql);
	}

	public void likeDelete(String relTypeCode, int relId, int memberId) {
		SecSql sql = new SecSql();
		sql.append("DELETE FROM islike");
		sql.append("WHERE relTypeCode = ?", relTypeCode);
		sql.append("AND relId = ?", relId);
		sql.append("AND memberId = ?", memberId);
		sql.append("AND `point` = 1");

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
