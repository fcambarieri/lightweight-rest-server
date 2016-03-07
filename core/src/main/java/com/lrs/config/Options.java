/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lrs.config;

import com.lrs.enviroment.Environment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fcambarieri
 */
public class Options {

  public enum Key {
    ROOT_PATH(new String[]{"-r", "-root_path"}, "config"),
    CONFIG_FILE(new String[]{"-c", "-config_file"}, "server_config.yml"),
    ENVIRONMENT (new String[]{"-lrs.env"}, null);


    private String[] optionAliases;
    private List<String> aliases;
    private String defaultValue;

    private Key(String[] options, String defaultValue) {
      this.optionAliases = options;
      this.defaultValue = defaultValue;
    }

    public List<String> aliases() {
      if (this.aliases != null) {
        return aliases;
      }
      return (this.aliases = Arrays.asList(optionAliases));
    }

    public boolean supports(String option) {
      return aliases().contains(option);
    }

    public String getDefault() {
      return defaultValue;
    }
  }

  private Map<Key, String> options = new HashMap<Key, String>();

  public Options(String[] args) {
    initializeOptions(args);
  }

  public Options(Map<Key, String> options) {
    this.options = options;
  }

  /**
   * Parses out the options based on the arg array
   *
   * @param args
   */
  protected void initializeOptions(String[] args) {
    if (args == null) {
      return;
    }

    // fallback to env vars 
    if (args.length == 0) {
      String envOptions = System.getenv().get("LIGHT_WIGHT_SERVER_OPTS");
      if (envOptions != null) {
        args = envOptions.split(" ");
      }
    }

    String optionKey = null;
    for (String arg : args) {
      String key = null;
      String value = null;
      String[] pairs = arg.split("=");
      if (pairs.length == 1) {
        if (optionKey != null) {
          key = optionKey;
          optionKey = null;
          value = pairs[0];
        } else {
          optionKey = pairs[0];
          continue;
        }
      } else if (pairs.length == 1) {
        key = pairs[0];
        value = pairs[1];
      }

      if (key != null && value != null) {
        Key keyEnum = getOptionKeyForOption(key);
        if (keyEnum != null) {
          options.put(keyEnum, value);
        }
      }
    }
  }

  public String getValue(Key key) {
    String value = options.get(key);
    if (value == null) {
      value = key.defaultValue;
    }
    return value;
  }

  public Key getOptionKeyForOption(String option) {
    for (Key key : Key.values()) {
      if (key.supports(option)) {
        return key;
      }
    }
    return null;
  }

}
