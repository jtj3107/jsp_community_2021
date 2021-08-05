package com.jhs.exam.exam2.repository;

import java.util.List;

import com.jhs.exam.exam2.container.ContainerComponent;
import com.jhs.exam.exam2.dto.Member;
import com.jhs.mysqliutil.MysqlUtil;
import com.jhs.mysqliutil.SecSql;

public class MemberRepository implements ContainerComponent{
	public void init() {

	}

	public Member getMemberByLoginId(String loginId) {
		// 로그인아이디로 해당 member가 DB에 존재하는지 확인
		SecSql sql = new SecSql();
		sql.append("SELECT M.*");
		sql.append("FROM member AS M");
		sql.append("WHERE M.loginId = ?", loginId);

		return MysqlUtil.selectRow(sql, Member.class);
	}

	public int join(String loginId, String loginPw, String name, String nickname, String email, String cellphoneNo) {
		SecSql sql = new SecSql();
		// 입력받은 변수를 이용 새로운 member를 DB에서 생성
		sql.append("INSERT INTO `member`");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", loginId = ?", loginId);
		sql.append(", loginPw = ?", loginPw);
		sql.append(", `name` = ?", name);
		sql.append(", nickname = ?", nickname);
		sql.append(", email = ?", email);
		sql.append(", cellphoneNo = ?", cellphoneNo);

		return MysqlUtil.insert(sql);
	}

	public static List<Member> getForPrintMembers() {
		// DB에서 members를 구해 리턴
		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM `member`");

		return MysqlUtil.selectRows(sql, Member.class);
	}

	public Member getMemberByNameAndEmail(String name, String email) {
		// 해당 name과 email을 이용해 해당member를 찾아 리턴
		SecSql sql = new SecSql();
		sql.append("SELECT M.*");
		sql.append("FROM member AS M");
		sql.append("WHERE M.name = ?", name);
		sql.append("AND M.email = ?", email);
		sql.append("LIMIT 1");

		return MysqlUtil.selectRow(sql, Member.class);
	}

	public void modifyPassword(int id, String loginPw) {
		// 해당 멤버 DB에서 비밀번호 변경
		SecSql sql = new SecSql();
		sql.append("UPDATE member AS M");
		sql.append("SET loginPw = ?", loginPw);
		sql.append("WHERE M.id= ?", id);

		MysqlUtil.update(sql);
	}

	public void modify(int id, String loginPw, String name, String nickname, String email, String cellphoneNo) {
		SecSql sql = new SecSql();
		sql.append("UPDATE member AS M");
		sql.append("SET loginPw = ?", loginPw);
		sql.append(", name = ?", name);
		sql.append(", nickname = ?", nickname);
		sql.append(", email = ?", email);
		sql.append(", cellphoneNo = ?", cellphoneNo);
		sql.append("WHERE M.id= ?", id);

		MysqlUtil.update(sql);
		
	}

}
