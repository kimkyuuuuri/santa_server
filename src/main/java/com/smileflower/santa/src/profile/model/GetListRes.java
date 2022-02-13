package com.smileflower.santa.src.profile.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetListRes {
    private GetFlagCountRes getFlagCountRes;
    private List<GetMountainsRes> getMountainsRes;

}
