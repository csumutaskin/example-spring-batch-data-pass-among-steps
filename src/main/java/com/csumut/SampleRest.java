package com.csumut;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class SampleRest {

  @GetMapping("/get")
  @ResponseBody
  public String get() {
    return "abc";
  }
}