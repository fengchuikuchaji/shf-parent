package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Admin;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Role;
import com.atguigu.mapper.AdminMapper;
import com.atguigu.mapper.AdminRoleMapper;
import com.atguigu.mapper.RoleMapper;
import com.atguigu.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2022-06-13  10:29
 */
@Service(interfaceClass = AdminService.class)
public class AdminServiceImpl extends BaseServiceImpl<Admin> implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Override
    public BaseMapper<Admin> getEntityMapper() {
        return adminMapper;
    }

    @Override
    public void insert(Admin admin) {
        //判断用户名是否已被占用:根据username查询用户，如果能查询到数据说明用户名已被占用
        Admin usernameAdmin = adminMapper.getByUsername(admin.getUsername());
        if (usernameAdmin != null) {
            //说明用户名已被占用
            throw new RuntimeException("用户名已被占用");
        }
        //判断手机号是否已被占用
        Admin phoneAdmin = adminMapper.getByPhone(admin.getPhone());
        if (phoneAdmin != null) {
            //说明手机号已被占用
            throw new RuntimeException("手机号已被占用");
        }
        //如果用户名和手机号都没有被占用，则可以添加用户
        super.insert(admin);
    }

    @Override
    public List<Admin> findAll() {
        return adminMapper.findAll();
    }

    @Override
    public Map<String, List<Role>> findRolesByAdminId(Long id) {
        //1. 查询出所有角色列表
        List<Role> allRoleList = roleMapper.findAll();
        //2. 查询出该用户已分配的角色的id列表
        List<Long> roleIdList = adminRoleMapper.findRoleIdListByAdminId(id);
        //3. 从allRoleList找出已分配的角色列表
        List<Role> assignRoleList = allRoleList.stream()
                .filter(role -> roleIdList.contains(role.getId()))
                .collect(Collectors.toList());

        //4. 从allRoleList找出未分配的角色列表
        List<Role> unAssignRoleList = allRoleList.stream()
                .filter(role -> !roleIdList.contains(role.getId()))
                .collect(Collectors.toList());

        //5. 把已分配和未分配的角色列表放入map中
        Map<String,List<Role>> map = new HashMap<>();
        map.put("assignRoleList",assignRoleList);
        map.put("unAssignRoleList",unAssignRoleList);

        return map;
    }

    //复杂一些的做法
    @Override
    public void saveAssignRole(Long adminId, List<Long> roleIds) {
        //roleIds就是这次用户要分配的角色id
        //1. 删除要删除的分配角色
        //1.1 找出哪些是要删除的分配角色:先拿到该用户之前的已分配角色的id列表，
        List<Long> originRoleIds = adminRoleMapper.findRoleIdListByAdminId(adminId);
        //1.2 如果之前的已分配的角色id列表中的roleId不在现在的roleIds中，则说明该分配角色需要被删除
        List<Long> removeRoleIds = originRoleIds.stream()
                .filter(roleId -> !roleIds.contains(roleId))
                .collect(Collectors.toList());
        //1.3 调用持久层的方法进行删除
        if (!CollectionUtils.isEmpty(removeRoleIds)) {
            adminRoleMapper.deleteByAdminIdAndRoleIds(adminId,removeRoleIds);
        }

        //2. 针对那些要添加的分配角色:判断其之前是否分配过，如果没有分配过则新增，如果是之前分配过但是删除了，则只需要将isDeleted改为0即可
        roleIds.forEach(roleId -> {
            //遍历出每一个roleId
            //2.1 判断该角色是否之前分配过:根据adminId和roleId查询数据
            AdminRole adminRole = adminRoleMapper.findByAdminIdAndRoleId(adminId,roleId);
            if (adminRole == null) {
                //2.2 如果之前没有分配过，则新增
                adminRole = new AdminRole();
                adminRole.setAdminId(adminId);
                adminRole.setRoleId(roleId);

                adminRoleMapper.insert(adminRole);
            }else {
                //2.3 之前分配过
                if (adminRole.getIsDeleted() == 1) {
                    //2.3.1 之前分配过，但是删除了,则只需要将isDeleted改为0即可
                    adminRole.setIsDeleted(0);
                    adminRoleMapper.update(adminRole);
                }
            }
        });
    }

    @Override
    public Admin getByUsername(String username) {
        return adminMapper.getByUsername(username);
    }

    //简单做法
    /*@Override
    public void saveAssignRole(Long adminId, List<Long> roleIds) {
        //1. 先删除用户已分配的所有角色
        adminRoleMapper.deleteByAdminId(adminId);
        //2. 将要分配的数据新增
        adminRoleMapper.insertBatch(adminId,roleIds);
    }*/
}
