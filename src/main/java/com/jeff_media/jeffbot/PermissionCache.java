package com.jeff_media.jeffbot;

import com.jeff_media.jeffbot.config.Config;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.*;

public class PermissionCache extends Config {

    private static final Set<String> EMPTY_SET = Collections.emptySet();
    private static final Map<Long,Object> EMPTY_MAP = Collections.emptyMap();
    private final Map<String, Set<String>> perUserPermissions = new HashMap<>();
    private final Map<String, Set<String>> perRolePermissions = new HashMap<>();

    public PermissionCache() {
        super("permissions.yml");
        Map<Long,Object> perUser = (Map<Long, Object>) getMap().getOrDefault("per-user",EMPTY_MAP);
        if(perUser == null) perUser = EMPTY_MAP;
        Map<Long,Object> perRole = (Map<Long, Object>) getMap().getOrDefault("per-role",EMPTY_MAP);
        if(perRole == null) perRole = EMPTY_MAP;
        perUser.forEach((userId, perms) -> {
            if(perms != null) perUserPermissions.put(String.valueOf(userId), new HashSet<>((List<String>) perms));
        });
        perRole.forEach((roleId,perms) -> {
            if(perms != null) perRolePermissions.put(String.valueOf(roleId), new HashSet<>((List<String>) perms));
        });
    }

    public Set<String> getPermissions(Member member) {
        String userId = member.getId();
        Set<String> perms = new HashSet<>(perUserPermissions.getOrDefault(userId, EMPTY_SET));
        for(Role role : member.getRoles()) {
            perms.addAll(perRolePermissions.getOrDefault(role.getId(),EMPTY_SET));
        }
        return perms;
    }

    public LinkedHashMap<String,String> getPermissionsWithOrigin(Member member) {
        String userId = member.getId();
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        for(String permission : perUserPermissions.getOrDefault(userId,EMPTY_SET)) {
            if(!map.containsKey(permission)) {
                map.put(permission,"native");
            }
        }
        for(Role role : member.getRoles()) {
            for (String permission : perRolePermissions.getOrDefault(role.getId(),EMPTY_SET)) {
                map.put(permission,role.getAsMention());
            }
        }
        return map;
    }
}
