package com.process.jus.security.entities;

import java.util.ArrayList;
import java.util.List;

public class Response<T> {

  private T data;
  private List<String> erros;

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public List<String> getErros() {
    if (erros == null) {
      return new ArrayList<>();
    }
    return erros;
  }

  public void setErros(List<String> erros) {
    this.erros = erros;
  }

}
