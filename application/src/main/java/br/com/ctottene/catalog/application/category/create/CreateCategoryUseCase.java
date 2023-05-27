package br.com.ctottene.catalog.application.category.create;

import br.com.ctottene.catalog.application.UseCase;
import br.com.ctottene.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {

}
