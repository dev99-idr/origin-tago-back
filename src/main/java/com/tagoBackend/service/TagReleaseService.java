package com.tagoBackend.service;

import kr.co.j2.das.sql.DAS3;

import java.util.LinkedList;

public class TagReleaseService {
    DAS3 db = new DAS3("tago");

    public LinkedList getTagVersion() {
        try {
            String sql = "select * from tbl_version where version = (select max(version) from tbl_version);";

            LinkedList list = new LinkedList();
            LinkedList resultList = db.select(null, sql, list);

            if (resultList.size() < 1) throw new Exception();

            return resultList;
        } catch (Exception e) {
            return null;
        }
    }

    public LinkedList getTagRelease() {
        try {
            String sql = "select * from tbl_version order by release_time DESC";

            LinkedList list = new LinkedList();
            LinkedList resultList = db.select(null, sql, list);

            if (resultList.size() < 1) throw new Exception();

            return resultList;
        } catch (Exception e) {
            return null;
        }
    }
}
