package com.fitness.dashboard.auth;

import com.fitness.dashboard.entity.OTP;
import com.fitness.dashboard.entity.User;
import com.fitness.dashboard.repository.OTPRepository;
import com.fitness.dashboard.repository.UserRepository;
import com.fitness.dashboard.service.DashboardService;
import com.fitness.dashboard.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private DashboardService dashboardService;

    private final Random random = new Random();

    public SendOtpResponse sendOtp(String mobileNumber) {
        // Check if user exists, if not create
        User user = userRepository.findByMobileNumber(mobileNumber)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setMobileNumber(mobileNumber);
                    newUser.setName("User " + mobileNumber.substring(mobileNumber.length() - 4));
                    newUser.setCreatedAt(LocalDateTime.now());
                    return userRepository.save(newUser);
                });

        // Generate 6-digit OTP
        String otpCode = String.format("%06d", random.nextInt(1000000));

        // Save OTP
        OTP otp = new OTP();
        otp.setMobileNumber(mobileNumber);
        otp.setOtpCode(otpCode);
        otp.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otp.setVerified(false);
        otpRepository.save(otp);

        return new SendOtpResponse("OTP sent successfully", otpCode);
    }

    public VerifyOtpResponse verifyOtp(String mobileNumber, String otpCode) {
        OTP otp = otpRepository.findByMobileNumberAndVerifiedFalseOrderByExpiryTimeDesc(mobileNumber)
                .orElseThrow(() -> new RuntimeException("No OTP found"));

        if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        if (!otp.getOtpCode().equals(otpCode)) {
            throw new RuntimeException("Invalid OTP");
        }

        otp.setVerified(true);
        otpRepository.save(otp);

        User user = userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate data for first-time login
        dashboardService.generateUserData(user);

        String token = jwtUtil.generateToken(user.getId(), user.getMobileNumber());

        return new VerifyOtpResponse(token, "Login successful");
    }
}
