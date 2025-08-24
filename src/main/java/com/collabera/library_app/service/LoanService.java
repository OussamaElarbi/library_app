package com.collabera.library_app.service;

import com.collabera.library_app.dto.LoanBookDto;
import com.collabera.library_app.dto.ReturnBookDto;
import com.collabera.library_app.dto.response.LoanBookReceiptDto;
import com.collabera.library_app.dto.response.ReturnBookReceiptDto;

public interface LoanService {
    LoanBookReceiptDto loanBook(LoanBookDto loanBookDto);
    ReturnBookReceiptDto returnBook(ReturnBookDto returnBookDto);
}
