package co.com.japl.ea.utls;

import static org.pyt.common.constants.languages.Login.CONST_LANGUAGE_ERROR_REMEMBER_CREATION;
import static org.pyt.common.constants.languages.Login.CONST_LANGUAGE_ERROR_REMEMBER_DELETED;
import static org.pyt.common.constants.languages.Login.CONST_LANGUAGE_ERROR_REMEMBER_FIELD_CREATION;
import static org.pyt.common.constants.languages.Login.CONST_LANGUAGE_ERROR_REMEMBER_FIELD_INIT;
import static org.pyt.common.constants.languages.Login.CONST_LANGUAGE_ERROR_REMEMBER_FIN;
import static org.pyt.common.constants.languages.Login.CONST_LANGUAGE_ERROR_REMEMBER_INICIO;
import static org.pyt.common.constants.languages.Login.CONST_LANGUAGE_ERROR_REMEMBER_PASSWD;
import static org.pyt.common.constants.languages.Login.CONST_LANGUAGE_ERROR_REMEMBER_STATE;
import static org.pyt.common.constants.languages.Login.CONST_LANGUAGE_ERROR_REMEMBER_UPDATED;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;

import co.com.japl.ea.dto.system.UsuarioDTO;

public class LoginUtil {
	public static UsuarioDTO usuarioSystem;
	public static final String CONST_FILE_REMEMBER = "rememberme.bin";
	public static final String CONST_PATH_SAVES = "./";

	public final static UsuarioDTO loadLogin() throws IOException, ClassNotFoundException {
		Path path = Paths.get(CONST_PATH_SAVES, CONST_FILE_REMEMBER);
		if (!Files.exists(path)) {
			Files.createFile(path);
		}
		try (var is = Files.newInputStream(path); var file = new ObjectInputStream(is)) {
			var read = (UsuarioDTO) file.readObject();
			return read;
		}
	}

	public final static void writeRemember(UsuarioDTO remember) throws IOException {
		Path path = Paths.get(CONST_PATH_SAVES, CONST_FILE_REMEMBER);
		try (var os = Files.newOutputStream(path); var file = new ObjectOutputStream(os)) {
			file.writeObject(remember);
		}
	}

	public final static Boolean compareUsuariosRememberAndFound(UsuarioDTO found, UsuarioDTO remember) {
		Boolean valid = true;
		valid &= remember.getNombre().compareTo(found.getNombre()) == 0;
		valid &= remember.getCodigo().compareTo(found.getCodigo()) == 0;
		valid &= remember.getFechaCreacion().compareTo(found.getFechaCreacion()) == 0;
		valid &= remember.getFechaIncio().compareTo(found.getFechaIncio()) == 0;
		valid &= remember.getState().compareTo(found.getState()) == 0;
		return valid;
	}

	public final static void validUsuarioRememberFails(UsuarioDTO usuario) throws RuntimeException {
		if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
			throw new RuntimeException(CONST_LANGUAGE_ERROR_REMEMBER_PASSWD);
		}

		if (usuario.getFechaCreacion() != null) {
			var creation = LocalDate.ofInstant(usuario.getFechaCreacion().toInstant(), ZoneId.systemDefault());
			if (creation.compareTo(LocalDate.now()) > 0) {
				throw new RuntimeException(CONST_LANGUAGE_ERROR_REMEMBER_CREATION);
			}
		} else {
			throw new RuntimeException(CONST_LANGUAGE_ERROR_REMEMBER_FIELD_CREATION);
		}

		if (usuario.getFechaEliminacion() != null) {
			throw new RuntimeException(CONST_LANGUAGE_ERROR_REMEMBER_DELETED);
		}

		if (usuario.getFechaActualizacion() != null) {
			var updated = LocalDate.ofInstant(usuario.getFechaActualizacion().toInstant(), ZoneId.systemDefault());
			if (updated.compareTo(LocalDate.now()) >= 0) {
				throw new RuntimeException(CONST_LANGUAGE_ERROR_REMEMBER_UPDATED);
			}
		}

		if (usuario.getState() != 1) {
			throw new RuntimeException(CONST_LANGUAGE_ERROR_REMEMBER_STATE);
		}

		if (usuario.getFechaIncio() != null) {
			var updated = LocalDate.ofInstant(usuario.getFechaIncio().toInstant(), ZoneId.systemDefault());
			if (updated.compareTo(LocalDate.now()) > 0) {
				throw new RuntimeException(CONST_LANGUAGE_ERROR_REMEMBER_INICIO);
			}
		} else {
			throw new RuntimeException(CONST_LANGUAGE_ERROR_REMEMBER_FIELD_INIT);
		}

		if (usuario.getFechaFin() != null) {
			var updated = LocalDate.ofInstant(usuario.getFechaFin().toInstant(), ZoneId.systemDefault());
			if (updated.compareTo(LocalDate.now()) <= 0) {
				throw new RuntimeException(CONST_LANGUAGE_ERROR_REMEMBER_FIN);
			}
		}
	}

}
