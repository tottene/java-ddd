package br.com.ctottene.catalog.application.category.update;

import br.com.ctottene.catalog.application.UseCase;
import br.com.ctottene.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase
        extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {

}
