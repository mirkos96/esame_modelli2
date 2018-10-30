package org.personalized.dashboard.bootstrap;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import org.personalized.dashboard.auth.*;
import org.personalized.dashboard.dao.api.*;
import org.personalized.dashboard.dao.impl.*;
import org.personalized.dashboard.elasticsearch.ElasticsearchClient;
import org.personalized.dashboard.queue.*;
import org.personalized.dashboard.service.api.*;
import org.personalized.dashboard.service.impl.*;
import org.personalized.dashboard.utils.generator.ActivityGenerator;
import org.personalized.dashboard.utils.generator.IdGenerator;
import org.personalized.dashboard.utils.htmltidy.DOMParser;
import org.personalized.dashboard.utils.stopwords.StopwordsRemover;
import org.personalized.dashboard.utils.validator.*;

import static com.google.inject.matcher.Matchers.any;
import static com.google.inject.matcher.Matchers.inSubpackage;

/**
 * Created by sudan on 5/4/15.
 */
public class DIModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(BookmarkService.class).to(BookmarkServiceImpl.class);
        bind(DiaryService.class).to(DiaryServiceImpl.class);
        bind(NoteService.class).to(NoteServiceImpl.class);
        bind(PinService.class).to(PinServiceImpl.class);
        bind(TodoService.class).to(TodoServiceImpl.class);
        bind(ExpenseService.class).to(ExpenseServiceImpl.class);
        bind(DiaryService.class).to(DiaryServiceImpl.class);
        bind(ActivityService.class).to(ActivityServiceImpl.class);
        bind(SearchService.class).to(SearchServiceImpl.class);
        bind(TagService.class).to(TagServiceImpl.class);
        bind(UserService.class).to(UserServiceImpl.class);
        bind(SessionService.class).to(SessionServiceImpl.class);

        bind(BookmarkDao.class).to(BookmarkDaoImpl.class);
        bind(DiaryDao.class).to(DiaryDaoImpl.class);
        bind(NoteDao.class).to(NoteDaoImpl.class);
        bind(PinDao.class).to(PinDaoImpl.class);
        bind(TodoDao.class).to(TodoDaoImpl.class);
        bind(ExpenseDao.class).to(ExpenseDaoImpl.class);
        bind(DiaryDao.class).to(DiaryDaoImpl.class);
        bind(ActivityDao.class).to(ActivityDaoImpl.class);
        bind(TagDao.class).to(TagDaoImpl.class);
        bind(UserDao.class).to(UserDaoImpl.class);
        bind(SessionDao.class).to(SessionDaoImpl.class);

        bind(ValidationService.class).annotatedWith(Names.named("bookmark")).to
                (BookmarkValidationService.class);
        bind(ValidationService.class).annotatedWith(Names.named("batchSize")).to
                (BatchSizeValidationService.class);
        bind(ValidationService.class).annotatedWith(Names.named("note")).to(NoteValidationService
                .class);
        bind(ValidationService.class).annotatedWith(Names.named("pin")).to(PinValidationService
                .class);
        bind(ValidationService.class).annotatedWith(Names.named("todo")).to(TodoValidationService
                .class);
        bind(ValidationService.class).annotatedWith(Names.named("search")).to(SearchValidationService
                .class);
        bind(ValidationService.class).annotatedWith(Names.named("tag")).to(TagValidationService
                .class);
        bind(ValidationService.class).annotatedWith(Names.named("expense")).to(ExpenseValidationService
                .class);
        bind(ValidationService.class).annotatedWith(Names.named("diary")).to(DiaryValidationService
                .class);
        bind(ConstraintValidationService.class);

        bind(IdGenerator.class).asEagerSingleton();
        bind(ActivityGenerator.class).asEagerSingleton();

        bind(SessionManager.class);

        bind(ESIndexProducer.class).annotatedWith(Names.named("bookmark")).to(BookmarkESIndexProducer.class);
        bind(ESIndexProducer.class).annotatedWith(Names.named("note")).to(NoteESIndexProducer.class);
        bind(ESIndexProducer.class).annotatedWith(Names.named("todo")).to(TodoESIndexProducer.class);
        bind(ESIndexProducer.class).annotatedWith(Names.named("pin")).to(PinESIndexProducer.class);
        bind(ESIndexProducer.class).annotatedWith(Names.named("tag")).to(TagESIndexProducer.class);
        bind(ESIndexProducer.class).annotatedWith(Names.named("expense")).to(ExpenseESIndexProducer.class);
        bind(ESIndexProducer.class).annotatedWith(Names.named("page")).to(PageESIndexProducer.class);

        bind(ElasticsearchClient.class);

        bind(DOMParser.class);
        bind(StopwordsRemover.class);

        bind(GoogleLoginServlet.class).in(Singleton.class);
        bind(GoogleLoginCallbackServlet.class).in(Singleton.class);
        bind(DashboardServlet.class).in(Singleton.class);
        bind(LogoutServlet.class).in(Singleton.class);
        bind(LoginServlet.class).in(Singleton.class);

        bind(UserCookieGenerator.class);
        bind(RequestInterceptor.class);

        bindInterceptor(inSubpackage("org.personalized.dashboard.controller"), any(), new RequestInterceptor());

    }
}
