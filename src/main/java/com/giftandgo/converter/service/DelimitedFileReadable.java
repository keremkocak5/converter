package com.giftandgo.converter.service;

import java.io.InputStream;
import java.util.List;
import java.util.regex.Pattern;

public interface DelimitedFileReadable {

    List<String[]> read(InputStream inputStream, Pattern pattern);

}
