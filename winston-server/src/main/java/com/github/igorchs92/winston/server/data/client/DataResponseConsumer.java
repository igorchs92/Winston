package com.github.igorchs92.winston.server.data.client;

import com.github.igorchs92.winston.server.data.DataResponse;

/**
 * Created by Igor on 1-6-2017.
 */
public interface DataResponseConsumer {

    void consume(DataResponse dataResponse);

}
