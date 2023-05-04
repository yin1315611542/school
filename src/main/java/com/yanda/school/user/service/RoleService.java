package com.yanda.school.user.service;


import com.yanda.school.user.pojo.TbRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface RoleService {
    public ArrayList<HashMap> searchRoleOwnPermission(int id);
    public void insertRole(TbRole role);
    public void updateRolePermissions(TbRole role);
    public ArrayList<HashMap> searchAllPermission();
    public List<TbRole> searchAllRole();
    public void deleteRoleById(int id);

}
