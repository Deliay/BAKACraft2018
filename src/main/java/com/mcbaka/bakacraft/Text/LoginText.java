package com.mcbaka.bakacraft.Text;

import com.mcbaka.bakacraft.Main;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.net.URL;

public class LoginText {
    public static Text COMMAND_ARG_PASSWORD = Text.of("password");
    public static Text COMMAND_LOGIN = Text.of("login");
    public static Text REQUIRE_PASSWORD = Text.builder("必须输入密码").color(TextColors.RED).build();
    public static Text REQUIRE_LOGIN = Text.builder("你还没有登录，快使用 /login [你的密码] 来登陆").color(TextColors.YELLOW).build();
    public static Text ALREADY_LOGIN = Text.builder("你已经登录过了，不能再执行这个命令").color(TextColors.RED).build();
    public static Text LOGIN_TIPS = Text.builder("请在30秒内使用/login指令登录 ").color(TextColors.YELLOW).build();
    public static Text TOO_LONG_IDLE = Text.builder("你太长时间没有登录，被系统踢出").color(TextColors.RED).build();
    public static Text LOGIN_SERVICE_SHUTDOWN = Text.builder("登录通道被关闭，请稍后再试").color(TextColors.GREEN).build();
    public static Text LOGIN_AUTH_FAIL = Text.builder("用户名或密码不正确").color(TextColors.RED).build();
    public static Text LOGIN_SUCCESS = Text.builder("登录成功").color(TextColors.GREEN).build();
    public static Text EXIST_A_SAME_PLAYER = Text.builder("服务器已经有同名玩家").color(TextColors.RED).build();
    public static Text NOT_REGISTERED = Text.builder("你的账号还没有注册，请移步 https://mcbaka.com/ 进行注册").onClick(TextActions.openUrl(Main.offsite)).build();
}
