package com.happydiary.common.validation;

import javax.validation.GroupSequence;

@GroupSequence({ ValidationGroups.NotBlankGroup.class, ValidationGroups.SizeGroup.class, ValidationGroups.PatternGroup.class })
public interface ValidationSequence {

}
