package com.xmnjm.controller;

import com.xmnjm.model.TortWord;
import com.xmnjm.service.TortWordService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mandy.huang
 */
@RestController
@CrossOrigin(origins = "http://localhost:8082")
public class TortWordController {
    @Inject
    TortWordService tortWordService;

    @RequestMapping(value = "/api/tortWords/{id}", method = RequestMethod.GET)
    @ResponseBody
    TortWord findById(@PathVariable("id") Long id) {
        TortWord tortWord = tortWordService.findById(id);
        return tortWord;
    }

    @RequestMapping(value = "/api/tortWord", method = RequestMethod.POST)
    @ResponseBody
    Object save(@Valid @RequestBody TortWord tortWord) {
        Map<String, Object> result = new HashMap<>();
        List<TortWord> tortWords = tortWordService.findByTortWordName(tortWord.getTortWordName());
        if (!CollectionUtils.isEmpty(tortWords)) {
            result.put("result", "fail");
            result.put("message", "侵权词已经存在！");
        } else {
            tortWordService.save(tortWord);
            result.put("result", "success");
            result.put("tortWord",tortWord);
        }
        return result;
    }

    @RequestMapping(value = "/api/tortWords/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    Object delete(@PathVariable("id") Long id) {
        Map<String, Object> result = new HashMap<>();
        tortWordService.delete(id);
        result.put("result", "success");
        return result;

    }

    @RequestMapping(value = "/api/tortWords/{id}", method = RequestMethod.PUT)
    @ResponseBody
    Object update(@Valid @RequestBody TortWord tortWord) {
        Map<String, Object> result = new HashMap<>();
        String tortWordName = tortWord.getTortWordName();
        List<TortWord> tortWords = tortWordService.findByTortWordName(tortWordName);
        if(!CollectionUtils.isEmpty(tortWords)) {
            result.put("result", "fail");
            result.put("message", "侵权词不能重复！");
        } else {
            TortWord trtWrd = tortWordService.findById(tortWord.getId());
            trtWrd.setTortWordName(tortWord.getTortWordName());
            tortWordService.update(tortWord);
            result.put("result", "success");
            result.put("tortWord", trtWrd);
        }
        return result;
    }

    @RequestMapping(value = "/api/tort", method = RequestMethod.GET)
    @ResponseBody
    List<TortWord> list(@RequestParam String name) {
        TortWord tortWord = new TortWord();
        tortWord.setTortWordName(name);
        tortWord.setStatus(1);
        return tortWordService.list(tortWord, 0, Integer.MAX_VALUE, null);
    }

    @RequestMapping(value = "/api/tortWords", method = RequestMethod.GET)
    @ResponseBody
    List<TortWord> list() {
        TortWord tortWord = new TortWord();
        tortWord.setStatus(1);
        return tortWordService.list(tortWord, 0, Integer.MAX_VALUE, null);
    }
}
