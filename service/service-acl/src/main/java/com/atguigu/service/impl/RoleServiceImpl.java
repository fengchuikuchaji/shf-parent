package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Permission;
import com.atguigu.entity.Role;
import com.atguigu.entity.RolePermission;
import com.atguigu.mapper.PermissionMapper;
import com.atguigu.mapper.RoleMapper;
import com.atguigu.mapper.RolePermissionMapper;
import com.atguigu.service.RoleService;
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
 * 日期2022-06-11  10:37
 */
@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Override
    public BaseMapper<Role> getEntityMapper() {
        return roleMapper;
    }
    @Override
    public List<Role> findAll() {
        return roleMapper.findAll();
    }

    @Override
    public List<Map<String, Object>> findPermissionByRoleId(Long roleId) {
        //1. 查询出所有的权限
        List<Permission> allPermissionList = permissionMapper.findAll();
        //2. 查询出该角色拥有的权限的id
        List<Long> permissionIdList = rolePermissionMapper.findPermissionIdListByRoleId(roleId);

        //3. 要将allPermissionList中的每一个permission对象转成每一个map对象
        List<Map<String, Object>> zNodes = allPermissionList.stream()
                .map(permission -> {
                    //1. 创建一个map对象
                    Map<String, Object> map = new HashMap<>();
                    //2. 针对每一个map设置键值对
                    //2.1 设置id:其实就是当前permission对象的id
                    map.put("id", permission.getId());
                    //2.2 设置pid:其实就是当前permission对象的parentId
                    map.put("pId", permission.getParentId());
                    //2.3 设置name:其实就是当前permission对象的name
                    map.put("name", permission.getName());
                    //2.4 设置open:默认都设置为true
                    map.put("open", true);
                    //2.5 设置checked:如果当前权限已分配给当前角色，则设置为true
                    //如果当前permission的id在permissionIdList中，说明该权限已分配给当前角色，则设置为true
                    map.put("checked", permissionIdList.contains(permission.getId()));
                    return map;
                }).collect(Collectors.toList());

        return zNodes;
    }

    //复杂的实现方案
    @Override
    public void saveRolePermission(Long roleId, List<Long> permissionIds) {
        //1. 找出需要删除的角色分配权限:原来已分配，这次不要分配了
        //1.1 找出原来已分配的权限id
        List<Long> assignedPermissionIds = rolePermissionMapper.findPermissionIdListByRoleId(roleId);
        //1.2 assignedPermissionIds中的id，如果它不在permissionIds,说明它原来已分配，但现在不分配了,需要删除
        List<Long> removePermissionIds = assignedPermissionIds.stream()
                .filter(permissionId -> !permissionIds.contains(permissionId))
                .collect(Collectors.toList());
        //1.3 删除removePermissionIds中的角色分配权限
        if (!CollectionUtils.isEmpty(removePermissionIds)) {
            rolePermissionMapper.removeRolePermission(roleId,removePermissionIds);
        }

        //2. 给需要分配的权限(permissionIds)进行分配
        permissionIds.forEach(permissionId -> {
            //2.1 判断当前权限是否已分配过:根据roleId和permissionId查询(不考虑isDeleted)
            RolePermission rolePermission = rolePermissionMapper.findByRoleIdAndPermissionId(roleId,permissionId);
            if (rolePermission == null) {
                //之前从未被分配过，则新增数据
                rolePermission = new RolePermission();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId(permissionId);
                rolePermissionMapper.insert(rolePermission);
            }else {
                //说明之前已经分配过，判断这次需不需要重新分配
                if (rolePermission.getIsDeleted() == 1) {
                    //需要重新分配:修改isDeleted为0
                    rolePermission.setIsDeleted(0);
                    rolePermissionMapper.update(rolePermission);
                }
            }
        });
    }

    //简单实现方案
    /*@Override
    public void saveRolePermission(Long roleId, List<Long> permissionIds) {
        //1. 先删除当前角色分配的所有权限
        rolePermissionMapper.deleteByRoleId(roleId);
        //2. 给当前角色重新分配权限
        rolePermissionMapper.insertBatch(roleId, permissionIds);
    }*/
}
