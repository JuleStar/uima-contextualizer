package uima.sandbox.contextualizer.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

public class Context {
	
	private int scope;
	
	private void setScope(int scope) {
		this.scope = scope;
	}
	
	private int getScope() {
		return this.scope;
	}
	
	private List<Annotation> annotations;

	private void setAnnotations(FSIterator<Annotation> iterator) {
		this.annotations = new ArrayList<Annotation>();
		while (iterator.hasNext()) {
			this.annotations.add(iterator.next());
		}
	}

	private List<Annotation> getAnnotations() {
		return annotations;
	}
	
	public Context(JCas cas,int scope) {
		this.setScope(scope);
		Type type = cas.getTypeSystem().getType("TypeName");
		FSIterator<Annotation> iterator = cas.getAnnotationIndex(type).iterator();
		this.setAnnotations(iterator);
	}
	
	public List<List<Annotation>> make() {
		List<List<Annotation>> result = new ArrayList<List<Annotation>>();
		for (int index = 0; index < this.getAnnotations().size(); index++) {
			result.add(this.getAnnotations(index));
		}
		return result;
	}
	
	private Annotation getAnnotation(int index) {
		try {
			return this.getAnnotations().get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	private Annotation getPreviousAnnotation(int index,int step) {
		return this.getAnnotation(index - step);
	}
	
	private List<Annotation> getPreviousAnnotations(int index,int scope) {
		List<Annotation> annotations = new ArrayList<Annotation>();
		for (int step = scope; step > 0; step--) {
			Annotation annotation = this.getPreviousAnnotation(index,step);
			if (annotation != null) {
				annotations.add(annotation);
			}
		}
		return annotations;
	}
	
	private Annotation getNextAnnotation(int index,int step) {
		return this.getAnnotation(index + step);
	}
	
	private List<Annotation> getNextAnnotations(int index,int scope) {
		List<Annotation> annotations = new ArrayList<Annotation>();
		for (int step = 1; step <= scope; step++) {
			Annotation annotation = this.getNextAnnotation(index,step);
			if (annotation != null) {
				annotations.add(annotation);	
			}
		}
		return annotations;
	}
	
	private List<Annotation> getAnnotations(int index) {
		List<Annotation> annotations = new ArrayList<Annotation>();
		int scope = this.getScope();
		Annotation annotation = this.getAnnotation(index);
		List<Annotation> previous = this.getPreviousAnnotations(index,scope);
		List<Annotation> next = this.getNextAnnotations(index,scope);
		annotations.add(annotation);
		annotations.addAll(previous);
		annotations.addAll(next);
		return annotations;
	}
	
}
