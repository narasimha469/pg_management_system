package com.pgmanagement.service;




import com.pgmanagement.requestDtos.UserRequestDto;
import com.pgmanagement.responseDtos.AdminUserDashboardResponse;
import com.pgmanagement.responseDtos.UserResponseDto;

public interface UserService {
	
	public UserResponseDto   createUser(UserRequestDto dto);
	
	UserResponseDto approveOwner(Long ownerId);
	
	AdminUserDashboardResponse getAllUsersWithDashboard(int page, int size);
	
//	public UserResponseDto getUserById(Long id);
//	
//	Page<UserResponseDto> getAllUsers(int page, int size);
//	
//	UserResponseDto updateUser(Long id, UserRequestDto dto);
//	
//	public void deleteUser(Long id);
//	
//	UserResponseDto toggleUserStatus(Long id);

}
