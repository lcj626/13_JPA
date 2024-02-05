package com.ohgiraffers.section06.compositkey.subsection02.idclass;

import java.io.Serializable;
import java.util.Objects;



public class MemberPk implements Serializable { // Pk = 복합키 설정


    private int memberNo;


    private String memberId;

    public MemberPk() {
    }

    public MemberPk(int memberNo, String memberId) {
        this.memberNo = memberNo;
        this.memberId = memberId;
    }

    public int getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberPk memberPk = (MemberPk) o;
        return memberNo == memberPk.memberNo && Objects.equals(memberId, memberPk.memberId);
    }// 해시코드 안하면 주소값을 반환하고 그럼 계속 Assertions.assertEquals가 false가 나온다

    @Override
    public int hashCode() {
        return Objects.hash(memberNo, memberId);
    }

    @Override
    public String toString() {
        return "MemberPk{" +
                "memberNo=" + memberNo +
                ", memberId='" + memberId + '\'' +
                '}';
    }
}
