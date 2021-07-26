package com.jhs.exam.exam2.repository;

import java.util.List;

import com.jhs.exam.exam2.dto.Article;
import com.jhs.exam.exam2.dto.Member;
import com.jhs.mysqliutil.MysqlUtil;
import com.jhs.mysqliutil.SecSql;

public class MemberRepository {

	public Member getMemberByLoginId(String loginId) {
		SecSql sql = new SecSql();
		sql.append("SELECT M.*");
		sql.append("FROM member AS M");
		sql.append("WHERE M.loginId = ?", loginId);
		
		return MysqlUtil.selectRow(sql, Member.class);
	}

	public void join(String loginId, String loginPw, String name, String nickname, String email, String cellphoneNo) {
		SecSql sql = new SecSql();
		sql.append("INSERT INTO `member`");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", loginId = ?", loginId);
		sql.append(", loginPw = ?", loginPw);
		sql.append(", `name` = ?", name);
		sql.append(", nickname = ?", nickname);
		sql.append(", email = ?", email);
		sql.append(", cellphoneNo = ?", cellphoneNo);
		
		MysqlUtil.insert(sql);
	}

	public static List<Member> getForPrintMembers() {
		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM `member`");
		
		return MysqlUtil.selectRows(sql, Member.class);
	}
	
	public Member getMemberByNameAndEmail(String name, String email) {
		SecSql sql = new SecSql();
		sql.append("SELECT M.*");
		sql.append("FROM member AS M");
		sql.append("WHERE M.name = ?", name);
		sql.append("AND");
		sql.append("email = ?", email);

		return MysqlUtil.selectRow(sql, Member.class);
	}

	public Member getMemberByLoginIdAndEmail(String loginId, String email) {
		// 해당 로그인아이디와 이메일에 맞는 멤버 DB에서 찾아 리턴
		SecSql sql = new SecSql();
		sql.append("SELECT M.*");
		sql.append("FROM member AS M");
		sql.append("WHERE M.loginId = ?", loginId);
		sql.append("AND");
		sql.append("email = ?", email);

		return MysqlUtil.selectRow(sql, Member.class);
	}

	public void setTempPassword(Member actor, String tempPassword) {
		// 해당 멤버 DB에서 비밀번호 변경
		SecSql sql = new SecSql();
		sql.append("UPDATE member AS M");
		sql.append("SET loginPw = ?", tempPassword);
		sql.append("WHERE M.id= ?", actor.getId());
		
		MysqlUtil.update(sql);
	}

}
