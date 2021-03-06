/*
 * Copyright (c) 2013, Klaus Boeing & Michel Graciano.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the project's authors nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS AND/OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package tdc2013.link.processors;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Completion;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Request;
import tdc2013.link.Documentation;

@SupportedAnnotationTypes({"tdc2013.link.Documentation"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class DocumentationProcessor extends AbstractProcessor {

    private static String[][] urls = new String[][]{
        new String[]{"http://wiki.netbeans.org/", "Wiki do projeto NetBeans.org."},
        new String[]{"http://www.wikipedia.org/", "Wiki do projeto Wikipedia - The Free Encyclopedia."},
        new String[]{"http://wiki.suaempresa.com.br/", "Wiki interna da sua empresa."}};

    @Override
    public boolean process(final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Documentation.class)) {
            final Documentation documentation = element.getAnnotation(Documentation.class);
            final String hostname = documentation.value();
            try {
                final StatusLine status = Request.Head(hostname).connectTimeout(500).
                        execute().returnResponse().getStatusLine();
                if (status.getStatusCode() != 200) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING,
                            "A documentação informada em " + hostname
                            + " parece ter sido (re)movida. Favor verificar.", element);
                }
            } catch (IOException ex) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING,
                        "A documentação informada em " + hostname
                        + " parece ter sido (re)movida. Favor verificar.", element);
            }
        }
        return false;
    }

    @Override
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation, ExecutableElement member, String userText) {
        final Set<Completion> c = new LinkedHashSet<>(3);
        for (final String[] url : urls) {
            c.add(new Completion() {
                @Override
                public String getValue() {
                    return '\"' + url[0] + '\"';
                }

                @Override
                public String getMessage() {
                    return url[1];
                }
            });
        }
        return c;
    }
}
