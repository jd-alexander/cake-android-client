package com.waracle.androidtest.data.loaders;

/**
 * Created by joel on 3/12/18.
 */

public class Result<T> {
    T data;
    String error;

    public Result(T data)
    {
        this.data = data;
    }

    public Result(String error)
    {
      this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


}
