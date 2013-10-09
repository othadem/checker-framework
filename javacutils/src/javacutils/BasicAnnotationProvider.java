package javacutils;

import java.lang.annotation.Annotation;

import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

public class BasicAnnotationProvider implements AnnotationProvider {

    @Override
    public AnnotationMirror getDeclAnnotation(Element elt,
            Class<? extends Annotation> anno) {
        List<? extends AnnotationMirror> annotationMirrors = elt
                .getAnnotationMirrors();

        // Then look at the real annotations.
        for (AnnotationMirror am : annotationMirrors) {
            if (AnnotationUtils.areSameByClass(am, anno)) {
                return am;
            }
        }

        return null;
    }
}
