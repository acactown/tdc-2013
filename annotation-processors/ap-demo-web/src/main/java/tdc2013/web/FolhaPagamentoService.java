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
package tdc2013.web;

import javax.inject.Inject;
import javax.inject.Named;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import tdc2013.web.script.FolhaPagamentoGroovy;
import tdc2013.web.script.FolhaPagamentoJavaScript;
import tdc2013.web.script.FolhaPagamentoJavaScriptPython;

@Named
public class FolhaPagamentoService {

    @Inject FolhaPagamentoGroovy pagamentoGroovy;
    
    @Inject FolhaPagamentoJavaScript pagamentoJavaScript;
    
    @Inject FolhaPagamentoJavaScriptPython pagamentoPython;
    
    public String getResumoCalculoFolhaGroovy(Number salarioMensal, int diasTrabalhados, double horasExtras){
        StringBuilder builder = new StringBuilder();
        builder.append("Salario: ").append(pagamentoGroovy.calculaSalarioMensal(salarioMensal, diasTrabalhados)).append("\n");
        builder.append("HorasExtras: ").append(pagamentoGroovy.calculaHoraExtra(salarioMensal, horasExtras)).append("\n");
        builder.append("13 Salário: ").append(pagamentoGroovy.calcula13(salarioMensal)).append("\n\n");
       
        return builder.append(getEngineInfo(ScriptEngine.GROOVY)).toString();
    }

    public String getResumoCalculoFolhaJavaScript(Number salarioMensal, int diasTrabalhados, double horasExtras){
        StringBuilder builder = new StringBuilder();
        builder.append("Salario: ").append(pagamentoJavaScript.calculaSalarioMensal(salarioMensal, diasTrabalhados)).append("\n");
        builder.append("HorasExtras: ").append(pagamentoJavaScript.calculaHoraExtra(salarioMensal, horasExtras)).append("\n");
        builder.append("13 Salário: ").append(pagamentoJavaScript.calcula13(salarioMensal)).append("\n\n");
       
        return builder.append(getEngineInfo(ScriptEngine.JAVA_SCRIPT)).toString();
    }
    
    public String getResumoCalculoFolhaPython(Number salarioMensal, int diasTrabalhados, double horasExtras){
        StringBuilder builder = new StringBuilder();
        builder.append("Salario: ").append(pagamentoPython.calculaSalarioMensal(salarioMensal, diasTrabalhados)).append("\n");
        builder.append("HorasExtras: ").append(pagamentoPython.calculaHoraExtra(salarioMensal, horasExtras)).append("\n");
        builder.append("13 Salário: ").append(pagamentoPython.calcula13(salarioMensal)).append("\n\n");
       
        return builder.append(getEngineInfo(ScriptEngine.PYTHON)).toString();
    }
    
    public String getResumoCalculoFolha(String engine, Number salarioMensal, int diasTrabalhados, double horasExtras){
        if(ScriptEngine.GROOVY.isValueEquals(engine)){
            return getResumoCalculoFolhaGroovy(salarioMensal, diasTrabalhados, horasExtras);
        }else if(ScriptEngine.JAVA_SCRIPT.isValueEquals(engine)){
            return getResumoCalculoFolhaJavaScript(salarioMensal, diasTrabalhados, horasExtras);
        }else if(ScriptEngine.PYTHON.isValueEquals(engine)){
            return getResumoCalculoFolhaPython(salarioMensal, diasTrabalhados, horasExtras);
        }
        return "";
    }
    
    private String getEngineInfo(ScriptEngine engine){
        ScriptEngineFactory factory = new ScriptEngineManager().getEngineByName(engine.get()).getFactory(); 
        StringBuilder builder = new StringBuilder();
        
        builder.append("engine name=").append(factory.getEngineName());
        builder.append("\nengine version=").append(factory.getEngineVersion());
        builder.append("\nlanguage name=").append(factory.getLanguageName());
        builder.append("\nextensions=").append(factory.getExtensions());
        builder.append("\nlanguage version=").append(factory.getLanguageVersion());
        builder.append("\nnames=").append(factory.getNames());
        builder.append("\nmime types=").append(factory.getMimeTypes());
        
        return builder.toString();
    }
}
