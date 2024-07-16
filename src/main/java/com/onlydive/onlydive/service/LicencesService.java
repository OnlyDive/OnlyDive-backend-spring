package com.onlydive.onlydive.service;

import com.onlydive.onlydive.model.LicenceEnum;
import com.onlydive.onlydive.model.User;
import com.onlydive.onlydive.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class LicencesService {

    private final UserRepository userRepository;

    public List<LicenceEnum> getAllLicences() {
        return Arrays.asList(LicenceEnum.values());
    }

    public void updateLicence(Long uid, LicenceEnum licence) {
        User user = userRepository.findById(uid).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        user.setLicence(licence);
    }
}
