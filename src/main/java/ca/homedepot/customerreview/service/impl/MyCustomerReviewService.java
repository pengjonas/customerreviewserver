package ca.homedepot.customerreview.service.impl;

import java.util.List;

import ca.homedepot.customerreview.model.LanguageModel;
import ca.homedepot.customerreview.util.ServicesUtil;
import org.springframework.beans.factory.annotation.Autowired;

import ca.homedepot.customerreview.model.ProductModel;
import ca.homedepot.customerreview.model.UserModel;
import ca.homedepot.customerreview.service.CustomerReviewService;
import ca.homedepot.customerreview.dao.CustomerReviewDao;
import ca.homedepot.customerreview.model.CustomerReviewModel;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;
import ca.homedepot.customerreview.exception.MyException;
import org.springframework.core.annotation.Order;

@Component("1")
public class MyCustomerReviewService implements CustomerReviewService {

    private CustomerReviewDao customerReviewDao;

    @Autowired
    public MyCustomerReviewService(CustomerReviewDao customerReviewDao) {
        this.customerReviewDao = customerReviewDao;
    }

    protected boolean internalValid(CustomerReviewModel review) {
        String regex = "\\b(ship)\\b|\\b(miss)\\b|\\b(duck)\\b|\\b(punt)\\b|\\b(rooster)\\b|\\b(mother)\\b|\\b(bits)\\b";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        if (review.getHeadline().matches(regex) || review.getRating() <= 0 || review.getComment().matches(regex)) {
            return false;
        }
        return true;
    }

    @Override
    public CustomerReviewModel createCustomerReview(final Double rating, final String headline, final String comment,
            final ProductModel product, final UserModel user) {
        final CustomerReviewModel review = new CustomerReviewModel();
        review.setRating(rating);
        review.setHeadline(headline);
        review.setComment(comment);
        review.setProduct(product);
        review.setUser(user);
        if (!internalValid(review)) {
            throw new MyException(null);
        }
        customerReviewDao.save(review);
        return review;
    }

    @Override
    public void updateCustomerReview(final CustomerReviewModel review, UserModel user, ProductModel product) {
        if (!internalValid(review)) {
            throw new MyException(review.getId());
        }
        customerReviewDao.save(review);
    }

    @Override
    public List<CustomerReviewModel> getReviewsForProduct(final ProductModel product) {
        ServicesUtil.validateParameterNotNullStandardMessage("product", product);
        return customerReviewDao.getAllReviews(product);
    }

    @Override
    public Double getAverageRating(final ProductModel product) {
        return customerReviewDao.getAverageRating(product);
    }

    @Override
    public Integer getNumberOfReviews(final ProductModel product) {
        return customerReviewDao.getNumberOfReviews(product);
    }

    @Override
    public List<CustomerReviewModel> getReviewsForProductAndLanguage(final ProductModel product, final LanguageModel language) {
        ServicesUtil.validateParameterNotNullStandardMessage("product", product);
        ServicesUtil.validateParameterNotNullStandardMessage("language", language);
        return customerReviewDao.getReviewsForProductAndLanguage(product, language);
    }

    @Override
    public void deleteCustomerReview(final Long id) {
        customerReviewDao.deleteReview(id);
    }
}
