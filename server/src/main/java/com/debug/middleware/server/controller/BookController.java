package com.debug.middleware.server.controller;

import com.debug.middleware.server.entity.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件描述
 *
 * @author dingguo.an
 * @date 2022年04月14日 15:45
 */
@RestController
@RequestMapping("/book")
public class BookController {
    private static final Logger log = LoggerFactory.getLogger(BookController.class);
    //获取书籍对象信息
    @RequestMapping(value="info", method = RequestMethod.GET)
    public Book info(Integer bookNo, String bookName){
        Book book = new Book();
        book.setBookNo(bookNo);
        book.setName(bookName);

        return book;
    }
}
