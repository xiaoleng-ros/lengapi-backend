package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.UserInterfaceInfo;
import generator.service.UserInterfaceInfoService;
import generator.mapper.UserInterfaceInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author lengleng
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

}




